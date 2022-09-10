package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INStarkeCountyBParser extends DispatchSPKParser {

  public INStarkeCountyBParser() {
    super("STARKE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "dispatch75@co.starke.in.us";
  }

  private static final Pattern SPECIAL_CITY_PTN = Pattern.compile("(.*?) +(JASPER COUNTY)", Pattern.CASE_INSENSITIVE);
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
   if (! super.parseHtmlMsg(subject, body, data)) return false;
   Matcher match = SPECIAL_CITY_PTN.matcher(data.strAddress);
   if (match.matches()) {
     data.strAddress = match.group(1);
     if (data.strCity.isEmpty()) data.strCity = match.group(2);
   }
   return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    return INStarkeCountyParser.baseAdjustMapAddress(addr);
  }
}
