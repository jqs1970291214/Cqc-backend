package com.cqc.backend.controller;

import com.cqc.backend.exception.MyException;
import com.cqc.backend.model.News;
import com.cqc.backend.service.NewsService;
import com.cqc.backend.util.ResultEnum;
import com.cqc.backend.util.ResultUtil;
import com.cqc.backend.viewmodel.ApiResult;
import com.sun.deploy.net.HttpResponse;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * author:Junqson
 * create:2018/4/14 19:34
 * des:
 */
@RestController
public class NewsController {

    @Autowired
    NewsService newsService;


    /*
    @CrossOrigin 粒度跨域
    */
    @RequestMapping(value = "/newsList",method = RequestMethod.POST)
    public ApiResult getNewsListByType(@RequestParam("type") String type) {
        ApiResult apiResult = ResultUtil.success();
        List<News> newsList = newsService.getNewsListByType(type);
        //过滤掉content，只返回列表
        for(News news : newsList) {
            news.setContent("");
        }
        apiResult.put("total",newsList.size());
        apiResult.put("newsList",newsList);
        return apiResult;
    }

    @RequestMapping(value = "/getNewsById",method = RequestMethod.POST)
    public ApiResult getNewsById(@RequestParam("id") Integer id) {
        News news = newsService.getNewsById(id);
        ApiResult apiResult = ResultUtil.success();
        apiResult.put("news",news);
        return apiResult;
    }

    @RequestMapping(value = "/getNewsByTitle",method = RequestMethod.POST)
    public ApiResult getNewsByTitle(@RequestParam("title") String title){
        News news = newsService.getNewsByTitle(title);
        ApiResult apiResult = ResultUtil.success();
       // apiResult.put("news",news);
        return apiResult;

    }

    @RequestMapping(value = "/deleteByTitle",method = RequestMethod.POST)
    public ApiResult deleteByTitle(@RequestParam("title") String title) {
        News news = newsService.deleteByTitle(title);
        ApiResult result = ResultUtil.success();
        result.put("news",news);
        return result;

    }


    @RequestMapping(value = "/addNews",method = RequestMethod.POST)
    public ApiResult addNews(@RequestParam("title") String title, @RequestParam("type") String type,
                             @RequestParam("cover") String cover,
                             @RequestParam("content") String content, HttpSession session) {
        //if (session.getAttribute("isOk") != "ok") throw new MyException(ResultEnum.LOGIN_REQUIRED);
        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(type)) {
            ApiResult apiResult = ResultUtil.error(ResultEnum.PARAM_EMPTY);
            return apiResult;
        } else {
            News news = new News();
            news.setTitle(title);
            news.setType(type);
            news.setContent(content);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(new Date());
            news.setTime(time);
            news.setCover(cover);
            if(StringUtils.isEmpty(cover)) news.setCover("#");
            newsService.addNews(news);
            return ResultUtil.success();
        }
    }

    @RequestMapping("/logtest")
    @ResponseBody
    public ApiResult testLogin(HttpSession session){
        ApiResult result = ResultUtil.success();
        Enumeration<String> attributes = session.getAttributeNames();
        while(attributes.hasMoreElements()){
            String name = attributes.nextElement();
            result.put(name,session.getAttribute(name));
        }
        return result;
    }


}
