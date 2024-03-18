package net.anei.cadpage.parsers.ZCABC;

public class ZCABCSmithersParser extends ZCABCPrinceGeorgeParser {

  public ZCABCSmithersParser() {
    super("SMITHERS", "BC");
  }

  @Override
  public String getFilter() {
    return "donotreply@rdffg.bc.ca";
  }
}
