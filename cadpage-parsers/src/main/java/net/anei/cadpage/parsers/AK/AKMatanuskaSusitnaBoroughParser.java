package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.dispatch.DispatchFastAlertParser;


public class AKMatanuskaSusitnaBoroughParser extends DispatchFastAlertParser {
  
  public AKMatanuskaSusitnaBoroughParser() {
    super("MATANUSKA-SUSITNA BOROUGH", "AK");
  }
  
  @Override
  public String getFilter() {
    return "response@ak-matanuska-5-1.fastalerting.com";
  }
}
