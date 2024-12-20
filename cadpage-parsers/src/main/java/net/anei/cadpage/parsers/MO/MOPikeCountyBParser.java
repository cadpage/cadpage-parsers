package net.anei.cadpage.parsers.MO;


public class MOPikeCountyBParser extends MOLincolnCountyAParser {

  public MOPikeCountyBParser() {
    super("PIKE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "EnterpolAlerts@PikeCountySO.org,relay@pikemo911.org";
  }
}