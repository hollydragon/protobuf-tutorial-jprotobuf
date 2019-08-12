package com.example;

import java.io.FileInputStream;
import java.io.IOException;

import com.example.protos.AddressBookProtos.AddressBook;
import com.example.protos.AddressBookProtos.Person;
import com.example.protos.AddressBookProtos.Person.Phone;

public class ListPerson {
	
	/**
	 * Reads the entire address book from a file and prints all the information inside
	 * @param args address file path
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: ListPerson ADDRESS_BOOK_FILE");
			System.exit(-1);
		}
		// Read the existing address book
		try {
			AddressBook addressBook = AddressBook.parseFrom(new FileInputStream(args[0]));
			print(addressBook);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Iterates though all people in the AddressBook and prints info about them
	 * @param addressBook AddressBook
	 */
	static void print(AddressBook addressBook) {
		for(Person person : addressBook.getPersonsList()) {
			System.out.println("Person ID: " + person.getId());
			System.out.println("  Name: " + person.getName());
			if (person.hasEmail()) {
				System.out.println("  E-mail: " + person.getEmail());
			}
			for(Phone phone : person.getPhonesList()) {
				switch (phone.getPhoneType()) {
				case MOBILE:
					System.out.print("  MOBILE phone #: ");
					break;
				case HOME:
					System.out.print("  HOME phone #: ");
					break;
				case WORK:
					System.out.print("  WORK phone #: ");
					break;
				}
				System.out.println(phone.getNo());
			}
			
		}
	}
}
