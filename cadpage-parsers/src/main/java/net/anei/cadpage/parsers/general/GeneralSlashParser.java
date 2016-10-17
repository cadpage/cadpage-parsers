package net.anei.cadpage.parsers.general;

public class GeneralSlashParser extends GeneralParser {
  
  public GeneralSlashParser() {
    super("/");
  }
  
  @Override
  public String getLocName() {
    return "Generic (slash field separator)";
  }
}
