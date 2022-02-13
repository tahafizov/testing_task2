insert into cats_stat
with
    tail_mean as
        (select round(avg(tail_length), 2) as mean from cats),
    tail_mediana as
        (select percentile_cont(0.5) within group ( order by tail_length ) as mediana from cats),
    tail_mode as
        (with tail_length_group as
                  (select tail_length, count(1) as count from cats
                   group by tail_length),
              tail_length_group_max as
                  (select max(count) as max_count from tail_length_group)
         select array_agg(tail_length) as mode from tail_length_group, tail_length_group_max
         where count = tail_length_group_max.max_count),
    whiskers_mean as
        (select round(avg(whiskers_length), 2) as mean from cats),
    whiskers_mediana as
        (select percentile_cont(0.5) within group ( order by whiskers_length ) as mediana from cats),
    whiskers_mode as
        (with whiskers_length_group as
                  (select whiskers_length, count(1) as count from cats
                   group by whiskers_length),
              whiskers_length_group_max as
                  (select max(count) as max_count from whiskers_length_group)
         select array_agg(whiskers_length) as mode from whiskers_length_group, whiskers_length_group_max
         where count = whiskers_length_group_max.max_count)
select tail_mean.mean as tail_length_mean,
       tail_mediana.mediana as tail_length_median,
       tail_mode.mode as tail_length_mode,
       whiskers_mean.mean as whiskers_length_mean,
       whiskers_mediana.mediana as whiskers_length_median,
       whiskers_mode.mode as whiskers_length_mode
from tail_mean, tail_mediana, tail_mode, whiskers_mean,whiskers_mediana,whiskers_mode;
