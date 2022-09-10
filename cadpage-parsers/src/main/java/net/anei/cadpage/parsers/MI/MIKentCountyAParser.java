package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MIKentCountyAParser extends DispatchH03Parser {

  public MIKentCountyAParser() {
    super("KENT COUNTY", "MI");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@Kent911.org,ssiler@caledoniatownship.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*) (?:Med|Fire) Alert");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (subject.equalsIgnoreCase("BURN PERMIT")) {
      setFieldList("CALL INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCall = subject;
      data.strSupp = decodeHtmlSequence(body).trim();
      return true;
    }

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1).trim();

    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "I-96 EB WO DEAN LAKE-GT",      "+43.01519,-85.61512",
      "I-96 EB WO FULTON",            "+42.96480,-85.58169"
  });
}
