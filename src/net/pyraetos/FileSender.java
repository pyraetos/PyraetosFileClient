package net.pyraetos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import net.pyraetos.util.Sys;

public class FileSender extends Socket{
	
	private String localDir;
	private String remoteDir;
	private String password;
	private Socket remote;
	
	public FileSender(String localDir, String remoteDir, String password){
		this.localDir = localDir;
		this.remoteDir = remoteDir;
		this.password = password;
		PFCFrame.setSending(true);
		try{
			remote = new Socket("pyraetos.net", 524);
		} catch(Exception e){
			PFCFrame.addLine("An exception occured in connection!");
			PFCFrame.setSending(false);
			return;
		}
		PFCFrame.addLine("Sending file to server...");
		new InputThread();
		new OutputThread();
	}

	public class InputThread implements Runnable{
		
		public InputThread(){
			Sys.thread(this);
		}
		
		@Override
		public void run(){
			try{
				InputStream in = remote.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				PFCFrame.addLine("The server says: " + reader.readLine());
				remote.close();
			}catch(Exception e){
				PFCFrame.addLine("An exception occured in input!");
				PFCFrame.setSending(false);
				try{
					remote.close();
				}catch(IOException e1){}
			}
		}
	
	}
	
	public class OutputThread implements Runnable{
		
		public OutputThread(){
			Sys.thread(this);
		}
		
		@Override
		public void run(){
			try{
				File file = new File(localDir);
				if(!file.exists()) throw new Exception();
				OutputStream o = remote.getOutputStream();
				DataOutputStream out = new DataOutputStream(o);
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
				writer.println(password + remoteDir);
				writer.flush();
				List<Integer> buffer = new ArrayList<Integer>();
				FileInputStream in = new FileInputStream(file);
				PFCFrame.setProgress(-1);
				PFCFrame.setProgressString("Buffering...");
				int b = in.read();
				while(b != -1){
					buffer.add(b);
					b = in.read();
				}
				in.close();
				int size = buffer.size();
				out.writeInt(size);
				double written = 0;
				PFCFrame.setProgressString("Sending...");
				for(int i : buffer){
					out.write(i);
					PFCFrame.setProgress((int)(((++written) / size) * 100d));
				}
				PFCFrame.setProgress(0);
				PFCFrame.setProgressString("");
				PFCFrame.setSending(false);
				out.flush();
			}catch(Exception e){
				PFCFrame.addLine("An exception occured in output!");
				PFCFrame.setSending(false);
				try{
					remote.close();
				}catch(IOException e1){}
			}
		}
	}
}
