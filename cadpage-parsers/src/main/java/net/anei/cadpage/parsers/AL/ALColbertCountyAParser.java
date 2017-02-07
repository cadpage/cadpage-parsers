package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Colbert County, AL
 */
public class ALColbertCountyAParser extends FieldProgramParser {
  
  public ALColbertCountyAParser() {
    super("COLBERT COUNTY", "AL", 
          "CFS:ID! MSG:CALL! CALL/SDS+? EMPTY CITY? ADDR/Z ( APT:APT EMPTY | EMPTY ) ( CCSD:SKIP! | SRC! ) END");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getFilter() {
    return "dispatch@911email.net";
  }
  
  private static Pattern LOST_BRK_PTN = Pattern.compile(" +(?=[-A-Z0-9]+:$|CCSD:.*$)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911-Page")) return false;
    body = LOST_BRK_PTN.matcher(body).replaceFirst("\n\n");
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("([-A-Z0-9]+):", true);
    return super.getField(name);
  }
}
