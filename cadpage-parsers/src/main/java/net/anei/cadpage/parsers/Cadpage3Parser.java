package net.anei.cadpage.parsers;

public class Cadpage3Parser extends Cadpage2Parser {
  
  public Cadpage3Parser() {
   this("", "", CountryCode.US);
  }
  
  public Cadpage3Parser(String defCity, String defState) {
    this(defCity, defState, CountryCode.US);
  }

  public Cadpage3Parser(CountryCode country) {
    this("", "", country);
  }
  
  public Cadpage3Parser(String defCity, String defState, CountryCode country) {
    super(";", defCity, defState, country, false);
  }
  
  @Override
  public String getLocName() {
    return "Standard Cadpage Format C";
  }
}
