package net.anei.cadpage.parsers.MI;

public class MIIoniaCountyParser extends MIBarryCountyBParser {

  public MIIoniaCountyParser() {
    super("IONIA COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "noreplyzuercher@ioniacounty.org";
  }
}
