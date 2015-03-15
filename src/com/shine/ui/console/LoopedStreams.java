package com.shine.ui.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class LoopedStreams {
	private PipedOutputStream pipedOS = new PipedOutputStream();
	private boolean keepRunning = true;
	private ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream() {
		public void close() {
			keepRunning = false;
			try {
				super.close();
				pipedOS.close();
			} catch (IOException e) {
				// ��¼�������������
				// Ϊ�򵥼ƣ��˴�����ֱ�ӽ���
				System.exit(1);
			}
		}
	};

	private PipedInputStream pipedIS = new PipedInputStream() {
		public void close() {
			keepRunning = false;
			try {
				super.close();
			} catch (IOException e) {
				// ��¼�������������
				// Ϊ�򵥼ƣ��˴�����ֱ�ӽ���
				System.exit(1);
			}
		}
	};

	public LoopedStreams() throws IOException {
		pipedOS.connect(pipedIS);
		startByteArrayReaderThread();
	}

	public InputStream getInputStream() {
		return pipedIS;
	}

	public OutputStream getOutputStream() {
		return byteArrayOS;
	}

	private void startByteArrayReaderThread() {
		new Thread(new Runnable() {
			public void run() {
				while (keepRunning) {
					// �����������ֽ���
					if (byteArrayOS.size() > 0) {
						byte[] buffer = null;
						synchronized (byteArrayOS) {
							buffer = byteArrayOS.toByteArray();
							byteArrayOS.reset(); // ���������
						}
						try {
							// ����ȡ�������ݷ��͸�PipedOutputStream
							pipedOS.write(buffer, 0, buffer.length);
						} catch (IOException e) {
							// ��¼�������������
							// Ϊ�򵥼ƣ��˴�����ֱ�ӽ���
							System.exit(1);
						}
					} else
						// û�����ݿ��ã��߳̽���˯��״̬
						try {
							// ÿ��1��鿴ByteArrayOutputStream���������
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
				}
			}
		}).start();
	}
}
