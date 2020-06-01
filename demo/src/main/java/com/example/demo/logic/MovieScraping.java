package com.example.demo.logic;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.example.demo.dto.MovieDto;

@Component
public class MovieScraping {
	// TODO DBから取得
	private static Map<Integer, String> ORIGINAL_SITE_URL = new HashMap<Integer, String>() {
		{
			put(0, "");
			put(1, "");
		}
	};

	public MovieDto scrape(int originalSite, String siteUrl) {
		String url = siteUrl;
		MovieDto movie = new MovieDto();
		try {
			Document document = Jsoup.connect(url).get();
			Element ele = document.getElementById("tabShareAndEmbed");
			String iframe = ele.getElementsByTag("input").val();
			String iframeUrl = iframe.split("<iframe src=\"")[1].split("\" frameborder=")[0].trim();

			movie.setOriginalLink(iframeUrl);

			Elements elemnts = document.getElementsByTag("meta");
			for (Element e : elemnts) {
				String property = e.attr("property");
				if ("og:image".equals(property)) {
					String imgUrl = e.attr("content");
					movie.setImgUrl(imgUrl);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movie;
	}
}
