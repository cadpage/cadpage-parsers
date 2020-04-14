package net.anei.cadpage.parsers.IL;


import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


/**
 *  O'Fallon, IL
 */
public class ILOFallonParser extends DispatchA33Parser {
    
  public ILOFallonParser() {
    super("O'FALLON", "IL");
  }
  
  @Override
  public String getFilter() {
    return "OFALLON@PUBLICSAFETYSOFTWARE.NET,CENCOM@CO.ST-CLAIR.IL.US,NOREPLYOFALLON@itiusa.com";
  }
}
