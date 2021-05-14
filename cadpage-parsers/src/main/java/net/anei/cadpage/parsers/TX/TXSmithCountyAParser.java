package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Smith County, TX
 */
public class TXSmithCountyAParser extends DispatchA19Parser {
  
  public TXSmithCountyAParser() {
    super("SMITH COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "Dgeneric@smith-county.com,.pphosted.com";
  }

  private static final Pattern SUBJ_PTN = Pattern.compile("(DISPATCH) +(?=Incident #)");
  private static final Pattern FIELD_BRK_PTN = Pattern.compile(" +(?=CAD Call ID #:|Type:|Date/Time:)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJ_PTN.matcher(subject);
    if (match.lookingAt()) {
      body = FIELD_BRK_PTN.matcher(subject.substring(match.end())).replaceAll("\n") + '\n' + body;
      subject = match.group(1);
    }
    // TODO Auto-generated method stub
    return super.parseMsg(subject, body, data);
  }
  
 }
