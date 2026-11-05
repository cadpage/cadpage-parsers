package net.anei.cadpage.parsers.MI;

public class MIGogebicCountyParser extends MIIronCountyParser {

  public MIGogebicCountyParser() {
    super("GOGEBIC COUNTY", "MI");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
