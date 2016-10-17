package net.anei.cadpage.parsers.UT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class UTDavisCountyAParser extends MsgParser {
  
  private static final Pattern TIME_PAT = Pattern.compile("^\\d\\d:\\d\\d:\\d\\d .*");
  
  public UTDavisCountyAParser() {
    super("DAVIS COUNTY", "UT");
    setFieldList("CALL ADDR APT PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "paging@co.davis.ut.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    int ndx = 1;
    boolean good = false;
    for (String line : body.split("\n")) {
      line = line.trim();

      switch (ndx) {
      
      case 1:
        if (line.startsWith("paging@co.davis.ut.us")) break;
        data.strCall = line;
        ndx++;
        break;
        
      case 2:
        Parser p = new Parser(line);
        parseAddress(p.get(';'), data);
        String extra = p.get();
        if (extra.length() > 0) {
          char first = extra.charAt(0);
          if (! extra.contains(" ") &&
              (first == '#' || Character.isDigit(first))) {
            data.strApt = extra;
          } else {
            data.strPlace = extra;
          }
        }
        good = true;
        ndx++;
        break;
        
      case 3:
        if (line.startsWith("ProQA Medical Recommended Dispatch Level")) break;
        if (TIME_PAT.matcher(line).matches()) break;
        if (data.strSupp.length() > 0) data.strSupp += ' ';
        data.strSupp += line;
        break;
      }
    }
    
    return good;
  }
}
