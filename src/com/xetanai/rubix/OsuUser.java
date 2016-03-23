package com.xetanai.rubix;

public class OsuUser {
	private String error;
	private String user_id;
	private String username;
	private String count300;
	private String count100;
	private String count50;
	private String playcount;
	private String ranked_score;
	private String total_score;
	private String pp_rank;
	private String level;
	private String pp_raw;
	private String accuracy;
	private String count_rank_ss;
	private String count_rank_s;
	private String count_rank_a;
	private String country;
	private String pp_country_rank;
	
	public OsuUser()
	{}
	
	public String getError()
	{
		return error;
	}
	
	public String getId()
	{
		return user_id;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String get300s()
	{
		return count300;
	}
	
	public String get100s()
	{
		return count100;
	}
	
	public String get50s()
	{
		return count50;
	}
	
	public String getPlayCount()
	{
		return playcount;
	}
	
	public String getRankedScore()
	{
		return ranked_score;
	}
	
	public String getTotalScore()
	{
		return total_score;
	}
	
	public String getLevel()
	{
		return level;
	}
	
	public String getpp()
	{
		return pp_raw;
	}
	
	public String getAccuracy()
	{
		return accuracy.substring(0,5) +"%";
	}
	
	public String getRankA()
	{
		return count_rank_a;
	}
	
	public String getRankS()
	{
		return count_rank_s;
	}
	
	public String getRankX()
	{
		return count_rank_ss;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public String getCountryRank()
	{
		return pp_country_rank;
	}
	
	public String getRank()
	{
		return pp_rank;
	}
}
