insert into cat_colors_info
select color, count(1) as count from cats
group by color;