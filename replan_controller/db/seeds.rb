# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)
p = Project.create(name: "Project Name", description: "Bla, bla, bla", \
                    effort_unit: "hour", hours_per_effort_unit: 1, \
                    hours_per_week_and_full_time_resource: 40)
                    
s1 = p.skills.create(name: "Java", description: "Java Programming Language")
s2 = p.skills.create(name: "Ruby", description: "Ruby Programming Language")
s3 = p.skills.create(name: "JavaScript", description: "JavaScript Programming Language")

r1 = p.resources.create(name: "Alice", description: "Alice Alculdia", availability: 75)
r2 = p.resources.create(name: "Bob", description: "Bob Bonaplata", availability: 55)
r3 = p.resources.create(name: "Calvin", description: "Calvin California", availability: 90)

r1.skills << s1
r1.skills << s2
r2.skills << s2
r2.skills << s3
r3.skills << s1
r3.skills << s3

f1 = p.features.create(code: 111, name: "Fix auto upload", description: "Bla, bla, bla", \
                       deadline: Date.today + 10, priority: 5)
f2 = p.features.create(code: 222, name: "New login", description: "Bla, bla, bla", \
                       deadline: Date.today + 12, priority: 4)
f3 = p.features.create(code: 334, name: "Enrollment refactoring", description: "Bla, bla, bla", \
                       deadline: Date.today + 15, priority: 3)
f4 = p.features.create(code: 454, name: "New channel", description: "Bla, bla, bla", \
                       deadline: Date.today + 18, priority: 2)
f5 = p.features.create(code: 556, name: "Email reply", description: "Bla, bla, bla", \
                       deadline: Date.today + 20, priority: 1)
f6 = p.features.create(code: 666, name: "Fix user display", description: "Bla, bla, bla", \
                       deadline: Date.today + 23, priority: 4)
f7 = p.features.create(code: 701, name: "Profile picture", description: "Bla, bla, bla", \
                       deadline: Date.today + 30, priority: 5)

f3.depends_on << f1
f3.depends_on << f2

f1.required_skills << s1
f1.required_skills << s3
f2.required_skills << s1
f2.required_skills << s3

l1 = p.releases.create(name: "August Release", description: "Bla, bla, bla", deadline: "2016-08-31")
l2 = p.releases.create(name: "Summer Extraordinary", description: "Bla, bla, bla", deadline: "2016-09-20")

l1.resources << r1
l1.resources << r3
l2.resources << r2
l2.resources << r3

l1.features << f1
l1.features << f2