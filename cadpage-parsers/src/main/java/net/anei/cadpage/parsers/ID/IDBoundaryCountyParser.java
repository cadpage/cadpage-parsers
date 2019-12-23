package net.anei.cadpage.parsers.ID;

public class IDBoundaryCountyParser extends IDBinghamCountyParser {
  
  public IDBoundaryCountyParser() {
    super("BOUNDARY COUNTY", "ID");
  }
  
  @Override
  public String getFilter() {
    return "active911@boundarysheriff.org";
  }
}
