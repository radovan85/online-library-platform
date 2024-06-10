package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "books")
public class BookEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(nullable = false, length = 13)
	private String ISBN;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 40)
	private String publisher;

	@Column(nullable = false, length = 40)
	private String author;

	@Column(nullable = false, length = 90)
	private String description;

	@Column(nullable = false, length = 30)
	private String language;

	@Column(name = "published_year", nullable = false)
	private Integer publishedYear;

	@Column(name = "page_number", nullable = false)
	private Integer pageNumber;

	@Column(nullable = false)
	private Float price;

	@Column(name = "average_rating")
	private Float averageRating;

	@Column(nullable = false, length = 30)
	private String cover;

	@Column(nullable = false, length = 30)
	private String letter;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "genre_id", nullable = false)
	private BookGenreEntity genre;

	@OneToOne(mappedBy = "book", fetch = FetchType.EAGER, orphanRemoval = true)
	private BookImageEntity image;

	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "book")
	private List<ReviewEntity> reviews;

	@Transient
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
	private List<WishListEntity> wishLists;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(Integer publishedYear) {
		this.publishedYear = publishedYear;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public BookGenreEntity getGenre() {
		return genre;
	}

	public void setGenre(BookGenreEntity genre) {
		this.genre = genre;
	}

	public List<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
	}

	public List<WishListEntity> getWishLists() {
		return wishLists;
	}

	public void setWishLists(List<WishListEntity> wishLists) {
		this.wishLists = wishLists;
	}

	public BookImageEntity getImage() {
		return image;
	}

	public void setImage(BookImageEntity image) {
		this.image = image;
	}

}
