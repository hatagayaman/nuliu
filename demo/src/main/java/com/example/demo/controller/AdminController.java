package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.data.GBConstants;
import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.OriginalSiteDto;
import com.example.demo.dto.TagDto;
import com.example.demo.form.MovieForm;
import com.example.demo.logic.MovieScraping;
import com.example.demo.service.CategoryService;
import com.example.demo.service.MovieService;
import com.example.demo.service.OriginalSiteService;
import com.example.demo.service.TagService;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private MovieService movieService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TagService tagService;

	@Autowired
	private OriginalSiteService siteService;

	@Autowired
	private MovieScraping scraping;

	@ModelAttribute
	MovieForm setUpForm() {
		return new MovieForm();
	}

	@GetMapping
	public String index() {
		return "admin";
	}

	@RequestMapping(value = "/movielist/tweet")
	@ResponseBody
	public void tweet(Model model, int movieid, int type) throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		User user = twitter.verifyCredentials();
	}

	@RequestMapping(value = "/moviesource")
	public String movieinfo(Model model) {
		List<OriginalSiteDto> siteList = siteService.getOriginalSiteList();
		model.addAttribute("siteList", siteList);
		return "moviesource";
	}

	@RequestMapping(value = "/moviesource/scraping")
	public String getMovieInfo(Model model, @RequestParam("originalSite") int originalSite,
			@RequestParam("siteUrl") String siteUrl) {
		model.addAttribute("indexForm", new MovieForm());
		List<OriginalSiteDto> siteList = siteService.getOriginalSiteList();
		model.addAttribute("siteList", siteList);
		List<CategoryDto> categoryList = categoryService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		MovieDto movie = scraping.scrape(originalSite, siteUrl);
		model.addAttribute("movie", movie);
		model.addAttribute("delFlgMap", GBConstants.DEL_FLG_MAP);
		return "moviesource";
	}

	@RequestMapping(value = "/movielist")
	public String movielist(Model model) {
		List<MovieDto> movieList = movieService.getMovieListForAdmin();
		model.addAttribute("movieList", movieList);
		return "movielist";
	}

	@RequestMapping(value = "/movieForm")
	public String movieinsert(Model model, Integer movieid) {
		model.addAttribute("h2title", movieid == null ? "新規登録" : "動画編集");
		model.addAttribute("formurl", movieid == null ? "/admin/movieForm/insert" : "/admin/movieForm/edit");
		List<CategoryDto> categoryList = categoryService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		List<OriginalSiteDto> siteList = siteService.getOriginalSiteList();
		model.addAttribute("siteList", siteList);
		MovieDto movie = new MovieDto();
		if (movieid != null) {
			movie = movieService.getMovieDetailListForAdmin(movieid);
		}
		model.addAttribute("movie", movie);
		List<TagDto> tagDBList = (movieid == null) ? new ArrayList<>() : tagService.getTagListByMovieId(movieid, false);
		List<String> tagList = new ArrayList<>();
		for (TagDto tag : tagDBList) {
			tagList.add(tag.getName());
		}
		model.addAttribute("tagList", String.join(" ", tagList));
		model.addAttribute("delFlgMap", GBConstants.DEL_FLG_MAP);
		return "movieForm";
	}

	// 動画新規登録
	@RequestMapping(value = "/movieForm/insert")
	public String insert(@Validated @ModelAttribute MovieForm form, BindingResult result, Model model) {
		MovieDto dto = new MovieDto();
		BeanUtils.copyProperties(form, dto);
		movieService.insertMovie(dto);
		tagService.insertMovieTag(dto);
		movieService.insertMovieView(dto);
		return "redirect:/admin/movielist";
	}

	// 動画編集
	@RequestMapping(value = "/movieForm/edit")
	public String edit(@Validated @ModelAttribute MovieForm form, BindingResult result, Model model) {
		MovieDto dto = new MovieDto();
		BeanUtils.copyProperties(form, dto);
		movieService.updateMovie(dto);
		tagService.updateMovieTag(dto);
		return "redirect:/admin/movielist";
	}
}
