#!/bin/zsh

# 100만 행의 랜덤한 숫자로 이루어진 임시 파일 생성
echo "Generating temporary data file..."
# seq 900000 > temp_data.txt
seq 10 > temp_data.txt

# 6개의 열을 가진 임시 파일 생성
echo "Generating temporary data with 6 columns..."

awk 'BEGIN{srand();} {print "test_" int(rand()*1000000)","int(rand()*1000000)","int(rand()*1000000)","int(rand()*1000000)","int(rand()*1000000)","int(rand()*1000000)}' temp_data.txt > temp_data_6_columns.csv


# 임시 파일 삭제
echo "Cleaning up..."
rm temp_data.txt

echo "Temporary data generation complete."

