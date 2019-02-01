using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class Checkpoint
    {
        public int id { get; set; }
        public string name { get; set; }
        public string scan { get; set; }
        public string hint { get; set; }
        public double latitude { get; set; }
        public double longitude { get; set; }
        public double points { get; set; }

        public Checkpoint(int id, string name, string scan, string hint, double latitude, double longitude, double points)
        {
            this.id = id;
            this.name = name;
            this.scan = scan;
            this.hint = hint;
            this.latitude = latitude;
            this.longitude = longitude;
            this.points = points;
        }

        public Checkpoint()
        {
        }
    }
}
