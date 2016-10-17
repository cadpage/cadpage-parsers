package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA2Parser;





public class NYChenangoCountyParser extends DispatchA2Parser {
    
    public NYChenangoCountyParser() {
      super("CHENANGO COUNTY", "NY");
    }
    
    @Override
    public String getFilter() {
      return "cad@co.chenango.ny.us";
    }
    
    @Override
    public String adjustMapAddress(String addr) {
      addr = HGTS_PTN.matcher(addr).replaceAll("HEIGHTS");
      return super.adjustMapAddress(addr);
    }
    private static final Pattern HGTS_PTN = Pattern.compile("\\bHGTS\\b", Pattern.CASE_INSENSITIVE);
	}
	