package com.zhuinden.sparkexperiment;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.util.*;

@SpringBootApplication
public class SparkExperimentApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SparkExperimentApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {

	}
}
