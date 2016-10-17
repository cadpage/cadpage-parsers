package net.anei.cadpage.parsers.IL;


import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 *  O'Fallon, IL
 */
public class ILOFallonParser extends DispatchA9Parser {
    
  public ILOFallonParser() {
    super(null, "O'FALLON", "IL");
  }
  
  @Override
  public String getFilter() {
    return "ofallonfire@ofallon.org,jrunyan@ofallon.org,forwarding-noreply@google.com";
  }

}
