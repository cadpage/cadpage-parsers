package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAStTammanyParishCParser extends FieldProgramParser {

  public LAStTammanyParishCParser() {
    super("ST TAMMANY PARISH", "LA",
          "nature:CALL! Lat:GPS1! Lon:GPS2! CadNum:ID! Place:PLACE! Apt:APT! Location:ADDRCITYST! Notes:INFO! Units:UNIT! END", FLDPROG_XML);
  }

  @Override
  public String getFilter() {
    return "no-reply@stfd13.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("\"", "");
    return super.parseHtmlMsg(subject, body, data);
  }

}
