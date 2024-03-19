package net.anei.cadpage.parsers.MS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MSDeSotoCountyAParser extends DispatchSPKParser {


  public MSDeSotoCountyAParser() {
    super("DESOTO COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "E911.AutoSend@obms.us,Dispatch@hernandopolice.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace('0', 'O');
    return true;
  }

  private static final Pattern COR_PTN = Pattern.compile("\\bCOR\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr =  COR_PTN.matcher(addr).replaceAll("CORNER");
    return addr;
  }
}
