package com.mediscan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class PrescriptionScannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrescriptionScannerApplication.class, args);
		System.out.println("✅ MediScan AI Running on http://localhost:8082");
	}
}