package net.anei.cadpage.parsers.ID;

public class IDBenewahCountyParser extends IDBinghamCountyParser {

  public IDBenewahCountyParser() {
    super("BENEWAH COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "active911@benewahcounty.org";
  }
}
