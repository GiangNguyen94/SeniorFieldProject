require 'fileutils'

# (0...49).each do |x|
#   folder_n = x * 100 + 1
#   folder_name = "range#{folder_n}"
#   FileUtils::mkdir_p folder_name
#   (0..99).each do |y|
#     file_n = folder_n - 1 +y
#     file_name = "repos_#{file_n}.json"
#     FileUtils.mv(file_name, "#{folder_name}/#{file_name}")
#   end
# end

(0...49).each do |x|
  folder_n = x * 100 + 1
  folder_name = "range#{folder_n}"
  new_folder_name = "part#{x}"
  FileUtils.mv folder_name, new_folder_name
end