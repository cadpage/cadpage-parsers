package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class ILCookCountyFParser extends DispatchA27Parser {

  public ILCookCountyFParser() {
    super("COOK COUNTY", "IL", "[A-Z]+\\d*");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,noreply@everbridge.net,@norcomm911.com";
  }

  private static final Pattern PHONE_INFO_PTN = Pattern.compile("(\\(\\d{3}\\)\\d{3}-\\d{4})\\b *");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n\n______");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    Matcher match = PHONE_INFO_PTN.matcher(data.strSupp);
    if (match.lookingAt()) {
      data.strPhone = match.group(1);
      data.strSupp = data.strSupp.substring(match.end());
    }

    data.strSource = convertCodes(data.strSource, SRC_CODES);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("INFO", "PHONE INFO");
  }

  private static final Properties SRC_CODES = buildCodeTable(new String[]{
      "Cicero 911 Dispatch",        "C911",
      "Cicero FD",                  "CCFD",
      "Cicero PD",                  "CCPD",
      "Franklin Park FD",           "FPFD",
      "Melrose Park FD",            "MPFD",
      "Melrose Park Public Safety", "MPPS",
      "Mobile Stroke Unit 1",       "MSU1",
      "River Grove FD",             "RGFD",
      "Stickney 911 Dispatch",      "S911",
      "Stickney FD",                "STFD",
      "Stone Park FD",              "SPFD"

  });
}
