package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IADallasCountyCParser extends FieldProgramParser {

  public IADallasCountyCParser() {
    super("DALLAS COUNTY", "IA",
          "( EMPTY MRK1 SKIP ADDRCITY MRK2 EMPTY EMPTY! " +
          "| ADDRCITY " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "iar@dallascountyiowa.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    String call = subject.substring(27).trim();
    int pt = call.indexOf(',');
    if (pt >= 0) call = call.substring(0,pt).trim();
    data.strCall = call;
    int pt1 = body.indexOf("<body><div><div>");
    if (pt1 < 0) return false;
    pt1 += 16;
    int pt2 = body.indexOf("</div></div></body>", pt1);
    if (pt2 < 0) return false;
    body = body.substring(pt1, pt2).trim();
    return parseFields(body.split("~", -1), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MRK1")) return new SkipField("\\(XS: \\)", true);
    if (name.equals("MRK2")) return new SkipField("\\(  \\)", true);
    return super.getField(name);
  }
}
