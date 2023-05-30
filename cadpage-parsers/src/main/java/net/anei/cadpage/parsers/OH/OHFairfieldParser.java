package net.anei.cadpage.parsers.OH;

public class OHFairfieldParser extends OHButlerCountyBParser {

  public OHFairfieldParser() {
    super("BUTLER COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "csquare@fairfield-city.org";
  }

}
