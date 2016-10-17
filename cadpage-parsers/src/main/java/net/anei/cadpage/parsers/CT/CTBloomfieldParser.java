package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;

/**
 * Bloomfield, CT
 */
public class CTBloomfieldParser extends DispatchRedAlertParser {
  
  public CTBloomfieldParser() {
    super("BLOOMFIELD","CT");
    setupMultiWordStreets(
        "BLUE HILLS",
        "COTTAGE GROVE",
        "WEST DUDLEY TOWN"
   );
  }
}
