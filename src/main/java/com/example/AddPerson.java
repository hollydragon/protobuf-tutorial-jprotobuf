package com.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.example.protos.AddressBookProtos.AddressBook;
import com.example.protos.AddressBookProtos.Person;
import com.example.protos.AddressBookProtos.Person.Phone;
import com.example.protos.AddressBookProtos.Person.PhoneType;

public class AddPerson {
	/**
	 * Reads the entire address book from a file, adds one person based on user input, then writes it back out to the same file
	 * @param args address file path
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: AddPerson ADDRESS_BOOK_FILE");
			System.exit(-1);
		}
		AddressBook.Builder addressBuilder = AddressBook.newBuilder();
		// Read the existing address book
		try {
			addressBuilder.mergeFrom(new FileInputStream(args[0]));
			// Add an address
			addressBuilder.addPersons(promptForPerson(new BufferedReader(new InputStreamReader(System.in)), System.out));
			// Write the new address book back to disk
			FileOutputStream output = new FileOutputStream(args[0]);
			addressBuilder.build().writeTo(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(args[0] + ": File not exist, create a new file first!");
		}
	}
	
	/**
	 * fills in a Person message based on user input
	 * @param stdin  BufferedReader
	 * @param stdout PrintStream
	 * @return a Person message
	 */
	static Person promptForPerson(BufferedReader stdin, PrintStream stdout) {
		Person.Builder personBuilder = Person.newBuilder();
		try {
			stdout.print("Enter person ID: ");
			personBuilder.setId(Integer.valueOf(stdin.readLine()));

			stdout.print("Enter person name: ");
			personBuilder.setName(stdin.readLine());
			
			stdout.print("Enter person email (blank for none): ");
			String email = stdin.readLine();
			if (email.trim().length() > 0) {
				personBuilder.setEmail(email);
			}
			while (true) {
				stdout.println("Enter person phone (or leave blank to finish): ");
				String no = stdin.readLine();
				if (no.trim().length() == 0) {
					break;
				}
				Person.Phone.Builder phoneBuilder = Phone.newBuilder().setNo(no);
				stdout.print("Is this a MOBILE, HOME or WORK phone?");
				Person.PhoneType phoneType = PhoneType.valueOf(stdin.readLine());
				phoneBuilder.setPhoneType(phoneType);
				personBuilder.addPhones(phoneBuilder);
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		return personBuilder.build();
	}
	
}
