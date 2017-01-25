package net.anei.cadpage.parsers.ZAU;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class ZAUNewSouthWalesCParser extends MsgParser {
  
  public ZAUNewSouthWalesCParser() {
    super("MUDGEE", "NSW", CountryCode.AU);
    setFieldList("DATE TIME INFO CALL SRC");
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:(\\d\\d? [A-Z][a-z]+ \\d{4}) )?(\\d\\d:\\d\\d:\\d\\d) (.*) - ([A-Z ]+) - ([A-Z]+)");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("dd MMM yyyy");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    String date = match.group(1);
    if (date != null) setDate(DATE_FMT, date, data);
    
    data.strTime = match.group(2);
    data.strSupp = match.group(3);
    data.strCall = match.group(4);
    data.strSource = match.group(5);
    return true;
  }

}
