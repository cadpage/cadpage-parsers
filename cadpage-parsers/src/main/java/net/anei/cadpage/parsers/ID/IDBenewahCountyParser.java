package net.anei.cadpage.parsers.ID;

public class IDBenewahCountyParser extends IDBinghamCountyAParser {

  public IDBenewahCountyParser() {
    super("BENEWAH COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "active911@benewahcounty.org";
  }
}
