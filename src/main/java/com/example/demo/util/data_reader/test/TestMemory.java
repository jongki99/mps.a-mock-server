package com.example.demo.util.data_reader.test;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;

import lombok.extern.slf4j.Slf4j;

public class TestMemory {
	public static void main(String[] args) throws InvalidFormatException, IOException {
		for(int i = 0; i< 10; i++ ) {
			// Runnable 객체 생성
			Mem1 myRunnable1 = new Mem1("Thread "+i);
			Thread thread1 = new Thread(myRunnable1);
			thread1.start(); // 10개를 돌리면 90M 파일 자체가 이미 24M 인데... 어떻게 동작하는 걸까... 더 느려지겠지?
		}
	}
}

@Slf4j
class Mem1 implements Runnable, Closeable {
    private final String name;
	private OPCPackage opc;

    Mem1(String name) {
        this.name = name;
    }
    
    

    @Override
	public void close() throws IOException {
    	if ( this.opc != null ) {
    		try {
				this.opc.close();
			} catch (Exception e) {
				log.error("");
			}
    	}
	}



	@Override
    public void run() {
    	try {
			test();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        for (int i = 0; i < 10; i++) {
            System.out.println(name + ": " + i);
            try {
                Thread.sleep(1000); // 1초 동안 일시 정지
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void test() throws InvalidFormatException, IOException {
		String pinFilePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/g_100.xlsx";
		FileInputStream inputStream = new FileInputStream(pinFilePath);
		OPCPackage.open(inputStream);
		opc = OPCPackage.open(pinFilePath, PackageAccess.READ);
    }
	
}