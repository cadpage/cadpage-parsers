package net.anei.cadpage.parsers.ID;

public class IDBoundaryCountyParser extends IDBinghamCountyAParser {
  
  public IDBoundaryCountyParser() {
    super("BOUNDARY COUNTY", "ID");
  }
  
  @Override
  public String getFilter() {
    return "active911@boundarysheriff.org";
  }
}
