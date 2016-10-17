package net.anei.cadpage.parsers.general;

public class GeneralDashParser extends GeneralParser {
  
  public GeneralDashParser() {
    super("-");
  }
  
  @Override
  public String getLocName() {
    return "Generic (dash field separator)";
  }

}
