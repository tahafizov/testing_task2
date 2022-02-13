CREATE TYPE cat_color AS ENUM (
    'black',
    'white',
    'black & white',
    'red',
    'red & white',
    'red & black & white'
    );

CREATE TABLE cats (
      name varchar,
      color cat_color,
      tail_length int,
      whiskers_length int,
      PRIMARY KEY (name)
);

CREATE TABLE cat_colors_info (
    color cat_color UNIQUE,
    count int
);

CREATE TABLE cats_stat (
    tail_length_mean numeric,
    tail_length_median numeric,
    tail_length_mode integer[],
    whiskers_length_mean numeric,
    whiskers_length_median numeric,
    whiskers_length_mode integer[]
);


