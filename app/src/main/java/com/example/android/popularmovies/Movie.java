package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {

  private Boolean adult;
  private String posterPath;
  private String overview;
  private Date releaseDate;
  private Long id;
  private String originalTitle;
  private String originalLanguage;
  private String title;
  private String backdropPath;
  private Double popularity;
  private Integer voteCount;
  private Boolean video;
  private Double voteAverage;

  public Movie() {
  }

  public Movie(Parcel in) {
    if (in != null) {
      int adultByte = in.readInt();
      if (adultByte != 0) {
        adult = Boolean.TRUE;
      } else {
        adult = Boolean.FALSE;
      }
      posterPath = in.readString();
      overview = in.readString();
      long releaseDateTime = in.readLong();
      if (releaseDateTime != 0) {
        releaseDate = new Date(releaseDateTime);
      }
      id = in.readLong();
      originalTitle = in.readString();
      originalLanguage = in.readString();
      title = in.readString();
      backdropPath = in.readString();
      popularity = in.readDouble();
      voteAverage = in.readDouble();
      voteCount = in.readInt();
      int videoByte = in.readInt();
      if (videoByte != 0) {
        video = Boolean.TRUE;
      } else {
        video = Boolean.FALSE;
      }
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt((adult != null && !adult)?1:0);
    parcel.writeString(posterPath);
    parcel.writeString(overview);
    parcel.writeLong((releaseDate != null)?releaseDate.getTime():0L);
    parcel.writeLong(id);
    parcel.writeString(originalTitle);
    parcel.writeString(originalLanguage);
    parcel.writeString(title);
    parcel.writeString(backdropPath);
    parcel.writeDouble(popularity);
    parcel.writeDouble(voteAverage);
    parcel.writeInt(voteCount);
    parcel.writeInt((video != null && !video.booleanValue())?1:0);
  }

  public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel parcel) {
      return new Movie(parcel);
    }

    @Override
    public Movie[] newArray(int i) {
      return new Movie[i];
    }
  };

  public Boolean getAdult() {
    return adult;
  }

  public void setAdult(Boolean adult) {
    this.adult = adult;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOriginalLanguage() {
    return originalLanguage;
  }

  public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public Double getPopularity() {
    return popularity;
  }

  public void setPopularity(Double popularity) {
    this.popularity = popularity;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Boolean getVideo() {
    return video;
  }

  public void setVideo(Boolean video) {
    this.video = video;
  }

  public Double getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(Double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public Integer getVoteCount() {
    return voteCount;
  }

  public void setVoteCount(Integer voteCount) {
    this.voteCount = voteCount;
  }
}
