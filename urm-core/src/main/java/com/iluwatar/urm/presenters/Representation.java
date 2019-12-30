package com.iluwatar.urm.presenters;

/**
 * Created by moe on 15.04.16.
 */
public class Representation {

  private final String content;
  private final String fileEnding;

  public Representation(String content, String fileEnding) {
    this.content = content;
    this.fileEnding = fileEnding;
  }

  public String getContent() {
    return content;
  }

  public String getFileEnding() {
    return fileEnding;
  }

}
