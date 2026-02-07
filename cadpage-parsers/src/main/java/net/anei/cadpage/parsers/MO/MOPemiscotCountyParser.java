package net.anei.cadpage.parsers.MO;

public class MOPemiscotCountyParser extends MONewMadridCountyParser {

  public MOPemiscotCountyParser() {
    super("PEMISCOT COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "NO_REPLY@MOPEMISCOTT.MMMICRO.COM";
  }
}
