package net.anei.cadpage.parsers.CT;

public class CTBethelBParser extends CTMiddletownParser {

  public CTBethelBParser() {
    super("BETHEL", "CT");
  }

  @Override
  public String getFilter() {
    return "NexgenAlerts@bethelpd.com";
  }
}
