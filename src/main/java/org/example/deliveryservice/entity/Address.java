package org.example.deliveryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.deliveryservice.entity.auth.AuthUser;

import java.util.List;

@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	private String street;

	@NotBlank
	@Size(min = 5, message = "Building name must contain atleast 5 characters")
	private String buildingName;

	@NotBlank
	@Size(min = 4, message = "City name must contain atleast 4 characters")
	private String city;

	@NotBlank
	@Size(min = 2, message = "State name must contain atleast 2 characters")
	private String state;

	@NotBlank
	@Size(min = 2, message = "Country name must contain atleast 2 characters")
	private String country;

	@NotBlank
	@Size(min = 6, message = "Pincode must contain atleast 6 characters")
	private String pincode;

	@ManyToMany(mappedBy = "addresses")
	private List<AuthUser> authUsers;

	public Address(String country, String state, String city, String pincode, String street, String buildingName) {
		this.country = country;
		this.state = state;
		this.city = city;
		this.pincode = pincode;
		this.street = street;
		this.buildingName = buildingName;
	}

}
