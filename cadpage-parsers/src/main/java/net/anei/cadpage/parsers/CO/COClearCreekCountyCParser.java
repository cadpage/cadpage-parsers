package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class COClearCreekCountyCParser extends MsgParser {

  public COClearCreekCountyCParser() {
    super("CLEAR CREEK COUNTY", "CO");
    setFieldList("CALL DATE TIME INFO");
  }

  @Override
  public String getFilter() {
    return "ro-clearcreek@readyop.com";
  }

  private static final Pattern MASTER1 = Pattern.compile("([-A-Za-z ]+?)- ?(\\d\\d:?\\d\\d)[- ]+(.*)", Pattern.DOTALL);
  private static final Pattern MASTER2 = Pattern.compile("([A-Za-z ]+?) (\\d\\d:?\\d\\d)[- ]+(.*)", Pattern.DOTALL);
  private static final Pattern MASTER3 = Pattern.compile("([A-Z ]+),? (\\d\\d?/\\d\\d?/\\d{4}) (?:at )?(\\d\\d:?\\d\\d)[;, ]+(.*)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
  private static final Pattern MASTER4 = Pattern.compile("([A-Z0-9 ]+)- *(.*)", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Clear Creek County - Unified Notification")) return false;
    int pt =  body.indexOf("\n\nYou are receiving");
    if (pt >= 0) body = body.substring(0,pt).trim();
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strTime = fixTime(match.group(2));
      data.strSupp = match.group(3);
    }

    else if ((match = MASTER2.matcher(body)).matches()) {
      data.strCall = match.group(1).trim();
      data.strTime = fixTime(match.group(2));
      data.strSupp = match.group(3);
    }

    else if ((match = MASTER3.matcher(body)).matches()) {
      data.strCall = match.group(1).trim();
      data.strDate = match.group(2);
      data.strTime = fixTime(match.group(3));
      data.strSupp = match.group(4);
    }

    else if ((match = MASTER4.matcher(body)).matches()) {
      data.strCall = match.group(1).trim();
      data.strSupp = match.group(2).trim();
    }

    else {
      data.strSupp = body;
    }
    return true;
  }

  private String fixTime(String time) {
    if (time.length() == 4) time = time.substring(0,2) + ':' + time.substring(2);
    return time;
  }

}
