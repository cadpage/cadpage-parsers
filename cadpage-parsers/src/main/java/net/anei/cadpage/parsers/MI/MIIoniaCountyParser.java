package net.anei.cadpage.parsers.MI;

public class MIIoniaCountyParser extends MIMidlandCountyParser {
  
  public MIIoniaCountyParser() {
    super("IONIA COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "cfs-noreply@ioniacounty.org";
  }
}
