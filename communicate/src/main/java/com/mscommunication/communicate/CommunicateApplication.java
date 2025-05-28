package com.mscommunication.communicate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SpringBootApplication
public class CommunicateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunicateApplication.class, args);
	}


	@Bean
	public List<CountryDto> getCountries(){
		List<CountryDto> list = new ArrayList<>();
		list.add(new CountryDto("India"));
		list.add(new CountryDto("USA"));
		return list;
	}
}



@RestController
class Controller{
	@Autowired
	List<CountryDto> list ;
	@Autowired
	Repo repo ;
	@GetMapping(value = "/get",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CountryDto> get(){

		return list;
	}

	@DeleteMapping
	public  String delete(String s){
		return ""+list.remove(s);
	}

	@GetMapping(value = "/add/{newcountry}")
	public  String add(@PathVariable("newcountry")  String newcountry){
		boolean b = false;
		Supplier<Boolean> r = ()->{

			CountryDto nc = new CountryDto(newcountry);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			synchronized (this) {
				if(!list.contains(nc)) {
					list.add(nc);
					return true;
				}
			}
			return false;

        };
		synchronized (this){
			CountryDto temp = new CountryDto(newcountry);
			b = list.contains(temp);
		}
		if(b){
			return "completed";
		}else {

			CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(r);
			future.thenAccept((flag) -> System.out.println("Adding country completed" + flag));
			return "pending..";
		}


	}
}


class CountryDto{
	String name;
	public CountryDto(String name){this.name = name;}

	public String getName(){
		return name;
	}
	@Override
	public boolean equals(Object dto){
		if(dto instanceof CountryDto) {
			return this.name.equals((((CountryDto) dto).name));
		}
		return false;
	}
}
@Repository
interface Repo extends JpaRepository<Countries,Integer> {

}

@Entity

class Countries{
	@Id

	int id;

	String countryname;
}