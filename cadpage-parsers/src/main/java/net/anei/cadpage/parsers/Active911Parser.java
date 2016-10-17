package net.anei.cadpage.parsers;

public class Active911Parser extends Cadpage2Parser {
  
  public Active911Parser() {
    super("\n", "", "", CountryCode.US, true);
  }
  
  @Override
  public String getLocName() {
    return "Active911 format";
  }
}
