package net.anei.cadpage.parsers.ID;

public class IDFremontCountyParser extends IDBinghamCountyAParser {
  
  public IDFremontCountyParser() {
    super("FREMONT COUNTY", "ID");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.fremont.id.us";
  }
}
